package jp.yoshida.photos_admin.controller;

import jp.yoshida.photos_admin.common.constant.KeyWordsConstants;
import jp.yoshida.photos_admin.common.constant.MessagesConstants;
import jp.yoshida.photos_admin.common.constant.UrlsConstants;
import jp.yoshida.photos_admin.common.exception.PhotosBusinessException;
import jp.yoshida.photos_admin.common.exception.PhotosSystemException;
import jp.yoshida.photos_admin.controller.form.DeleteForm;
import jp.yoshida.photos_admin.controller.form.PhotoForm;
import jp.yoshida.photos_admin.controller.form.PhotosForm;
import jp.yoshida.photos_admin.service.PhotosService;
import jp.yoshida.photos_admin.service.dto.PhotoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PhotosControllerTest {

    PhotosController photosController;

    @Mock
    PhotosService photosService;

    @Mock
    MessageSource messageSource;

    @Mock
    RedirectAttributes redirectAttributes;

    @Mock
    HttpServletRequest httpServletRequest;

    @Mock
    BindingResult bindingResult;

    PhotoDto photoDto1;

    PhotoDto photoDto2;

    List<PhotoDto> photoDtos;

    PhotoForm photoForm1;

    PhotoForm photoForm2;

    List<PhotoForm> photoForms;

    PhotosForm photosForm;

    DeleteForm deleteForm;

    final Integer id1 = 102;
    final Integer nextId1 = 103;
    final Integer prevId1 = 101;
    final String rawPhoto1 = "rawPhoto1";
    final String thumbnail1 = "thumbnail1";
    final String fileName1 = "fileName1";
    final String mimeType1 = "mymeType1";
    final String width1 = "width1";
    final String height1 = "height1";
    final String shootingDateTime1 = "shootingDateTime1";
    final String latitude1 = "latitude1";
    final String longitude1 = "longitude1";

    final Integer id2 = 202;
    final Integer nextId2 = 203;
    final Integer prevId2 = 201;
    final String rawPhoto2 = "rawPhoto2";
    final String thumbnail2 = "thumbnail2";
    final String fileName2 = "fileName2";
    final String mimeType2 = "mymeType2";
    final String width2 = "width2";
    final String height2 = "height2";
    final String shootingDateTime2 = "shootingDateTime2";
    final String latitude2 = "latitude2";
    final String longitude2 = "longitude2";


    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        photosController = new PhotosController(photosService, messageSource);

        photoDto1 = new PhotoDto();
        photoDto1.setId(id1);
        photoDto1.setNextId(nextId1);
        photoDto1.setPrevId(prevId1);
        photoDto1.setRawPhoto(rawPhoto1);
        photoDto1.setThumbnail(thumbnail1);
        photoDto1.setFileName(fileName1);
        photoDto1.setMimeType(mimeType1);
        photoDto1.setWidth(width1);
        photoDto1.setHeight(height1);
        photoDto1.setShootingDateTime(shootingDateTime1);
        photoDto1.setLatitude(latitude1);
        photoDto1.setLongitude(longitude1);

        photoDto2 = new PhotoDto();
        photoDto2.setId(id2);
        photoDto2.setNextId(nextId2);
        photoDto2.setPrevId(prevId2);
        photoDto2.setRawPhoto(rawPhoto2);
        photoDto2.setThumbnail(thumbnail2);
        photoDto2.setFileName(fileName2);
        photoDto2.setMimeType(mimeType2);
        photoDto2.setWidth(width2);
        photoDto2.setHeight(height2);
        photoDto2.setShootingDateTime(shootingDateTime2);
        photoDto1.setLatitude(latitude2);
        photoDto1.setLongitude(longitude2);

        photoDtos = new ArrayList<>();
        photoDtos.add(photoDto1);
        photoDtos.add(photoDto2);

        photoForm1 = new PhotoForm();
        BeanUtils.copyProperties(photoDto1, photoForm1);

        photoForm2 = new PhotoForm();
        BeanUtils.copyProperties(photoDto2, photoForm2);

        photoForms = new ArrayList<>();
        photoForms.add(photoForm1);
        photoForms.add(photoForm2);

        photosForm = new PhotosForm();
        photosForm.setPhotoForms(photoForms);

        deleteForm = new DeleteForm();
        deleteForm.setDeleteIds(new int[] {1, 2, 3});
    }

    @Test
    @DisplayName("写真一覧取得_正常系")
    void getPhotosN1() {

        PhotosForm requestedPhotosForm = new PhotosForm();
        doReturn(photoDtos).when(photosService).getPhotos();
        String actual = photosController.getPhotos(redirectAttributes, requestedPhotosForm);
        assertEquals(UrlsConstants.RESPONSE_GET_PHOTOS, actual);
        assertEquals(photosForm, requestedPhotosForm);
    }

    @Test
    @DisplayName("写真詳細取得_正常系")
    void getPhotoN1() throws PhotosBusinessException {

        PhotoForm requestedPhotoForm = new PhotoForm();
        int id = 1;
        doReturn(photoDto1).when(photosService).getPhoto(id);
        String actual = photosController.getPhoto(redirectAttributes, requestedPhotoForm, id, httpServletRequest);
        assertEquals(UrlsConstants.RESPONSE_GET_PHOTO, actual);
        assertEquals(photoForm1, requestedPhotoForm);
    }

    @Test
    @DisplayName("写真詳細取得_異常系_サービスから例外をキャッチ")
    void getPhotoE1() throws PhotosBusinessException {

        PhotoForm requestedPhotoForm = new PhotoForm();
        int id = 1;
        String exceptionMessage = "exceptionMessage";
        String infoLevel = "infoLevel";
        Locale locale = Locale.getDefault();
        String gotMessage = "gotMessage";
        doThrow(new PhotosBusinessException(exceptionMessage, infoLevel)).when(photosService).getPhoto(id);
        doReturn(locale).when(httpServletRequest).getLocale();
        doReturn(gotMessage).when(messageSource).getMessage(exceptionMessage, null, locale);
        String actual = photosController.getPhoto(redirectAttributes, requestedPhotoForm, id, httpServletRequest);
        assertEquals(UrlsConstants.REDIRECT_GET_PHOTOS, actual);
        verify(redirectAttributes, atLeastOnce()).addFlashAttribute(KeyWordsConstants.KEY_MESSAGE, gotMessage);
        verify(redirectAttributes, atLeastOnce()).addFlashAttribute(KeyWordsConstants.KEY_INFO_LEVEL, infoLevel);
    }

    @Test
    @DisplayName("写真登録_正常系")
    void addPhotoN1() throws PhotosBusinessException, PhotosSystemException {

        Locale locale = Locale.getDefault();
        String gotMessage = "gotMessage";
        doReturn(false).when(bindingResult).hasErrors();
        doNothing().when(photosService).addPhoto(photoForm1.getSendingPhoto());
        doReturn(locale).when(httpServletRequest).getLocale();
        doReturn(gotMessage).when(messageSource).getMessage(MessagesConstants.INFO_ADD_SUCCESS, null, locale);
        String actual = photosController.addPhoto(redirectAttributes, photoForm1, bindingResult, httpServletRequest);
        assertEquals(UrlsConstants.REDIRECT_GET_PHOTOS, actual);
        verify(redirectAttributes, atLeastOnce()).addFlashAttribute(KeyWordsConstants.KEY_MESSAGE, gotMessage);
        verify(redirectAttributes, atLeastOnce())
                .addFlashAttribute(KeyWordsConstants.KEY_INFO_LEVEL, MessagesConstants.INFO_LEVEL_INFO);
    }

    @Test
    @DisplayName("写真登録_異常系_バリデーションチェックエラー")
    void addPhotoE1() throws PhotosSystemException {

        doReturn(true).when(bindingResult).hasErrors();
        String actual = photosController.addPhoto(redirectAttributes, photoForm1, bindingResult, httpServletRequest);
        assertEquals(UrlsConstants.REDIRECT_GET_PHOTOS, actual);
        verify(redirectAttributes, atLeastOnce())
                .addFlashAttribute(KeyWordsConstants.NAME_BINDING_RESULT_PHOTO_FORM, bindingResult);
        verify(redirectAttributes, atLeastOnce()).addFlashAttribute(KeyWordsConstants.NAME_PHOTO_FORM, photoForm1);
    }

    @Test
    @DisplayName("写真登録_異常系_サービスから例外をキャッチ")
    void addPhotoE2() throws PhotosBusinessException, PhotosSystemException {

        String exceptionMessage = "exceptionMessage";
        String infoLevel = "infoLevel";
        Locale locale = Locale.getDefault();
        String gotMessage = "gotMessage";
        doReturn(false).when(bindingResult).hasErrors();
        doThrow(new PhotosBusinessException(exceptionMessage, infoLevel))
                .when(photosService).addPhoto(photoForm1.getSendingPhoto());
        doReturn(locale).when(httpServletRequest).getLocale();
        doReturn(gotMessage).when(messageSource).getMessage(exceptionMessage, null, locale);
        String actual = photosController.addPhoto(redirectAttributes, photoForm1, bindingResult, httpServletRequest);
        assertEquals(UrlsConstants.REDIRECT_GET_PHOTOS, actual);
        verify(redirectAttributes, atLeastOnce()).addFlashAttribute(KeyWordsConstants.KEY_MESSAGE, gotMessage);
        verify(redirectAttributes, atLeastOnce()).addFlashAttribute(KeyWordsConstants.KEY_INFO_LEVEL, infoLevel);
    }

    @Test
    @DisplayName("写真複数削除_正常系")
    void deletePhotosN1() throws PhotosBusinessException {

        Locale locale = Locale.getDefault();
        String gotMessage = "gotMessage";
        doReturn(false).when(bindingResult).hasErrors();
        doNothing().when(photosService).deletePhotos(deleteForm.getDeleteIds());
        doReturn(locale).when(httpServletRequest).getLocale();
        doReturn(gotMessage).when(messageSource).getMessage(MessagesConstants.INFO_DELETE_SUCCESS, null, locale);
        String actual = photosController.deletePhotos(redirectAttributes, deleteForm, bindingResult, httpServletRequest);
        assertEquals(UrlsConstants.REDIRECT_GET_PHOTOS, actual);
        verify(redirectAttributes, atLeastOnce()).addFlashAttribute(KeyWordsConstants.KEY_MESSAGE, gotMessage);
        verify(redirectAttributes, atLeastOnce())
                .addFlashAttribute(KeyWordsConstants.KEY_INFO_LEVEL, MessagesConstants.INFO_LEVEL_INFO);
    }

    @Test
    @DisplayName("写真複数削除_異常系_バリデーションチェックエラー")
    void deletePhotosE1() {

        doReturn(true).when(bindingResult).hasErrors();
        String actual = photosController
                .deletePhotos(redirectAttributes, deleteForm, bindingResult, httpServletRequest);
        assertEquals(UrlsConstants.REDIRECT_GET_PHOTOS, actual);
        verify(redirectAttributes, atLeastOnce())
                .addFlashAttribute(KeyWordsConstants.NAME_BINDING_RESULT_DELETE_FORM, bindingResult);
        verify(redirectAttributes, atLeastOnce()).addFlashAttribute(KeyWordsConstants.NAME_DELETE_FORM, deleteForm);
    }

    @Test
    @DisplayName("写真複数削除_異常系_サービスから例外をキャッチ")
    void deletePhotosE2() throws PhotosBusinessException {

        String exceptionMessage = "exceptionMessage";
        String infoLevel = "infoLevel";
        Locale locale = Locale.getDefault();
        String gotMessage = "gotMessage";
        doReturn(false).when(bindingResult).hasErrors();
        doThrow(new PhotosBusinessException(exceptionMessage, infoLevel))
                .when(photosService).deletePhotos(deleteForm.getDeleteIds());
        doReturn(locale).when(httpServletRequest).getLocale();
        doReturn(gotMessage).when(messageSource).getMessage(exceptionMessage, null, locale);
        String actual = photosController
                .deletePhotos(redirectAttributes, deleteForm, bindingResult, httpServletRequest);
        assertEquals(UrlsConstants.REDIRECT_GET_PHOTOS, actual);
        verify(redirectAttributes, atLeastOnce()).addFlashAttribute(KeyWordsConstants.KEY_MESSAGE, gotMessage);
        verify(redirectAttributes, atLeastOnce()).addFlashAttribute(KeyWordsConstants.KEY_INFO_LEVEL, infoLevel);
    }

    @Test
    @DisplayName("写真削除_正常系")
    void deletePhotoN1() throws PhotosBusinessException {

        int id = 1;
        Locale locale = Locale.getDefault();
        String gotMessage = "gotMessage";
        doNothing().when(photosService).deletePhotos(new int[]{id});
        doReturn(locale).when(httpServletRequest).getLocale();
        doReturn(gotMessage).when(messageSource).getMessage(MessagesConstants.INFO_DELETE_SUCCESS, null, locale);
        String actual = photosController.deletePhoto(redirectAttributes, id, httpServletRequest);
        assertEquals(UrlsConstants.REDIRECT_GET_PHOTOS, actual);
        verify(redirectAttributes, atLeastOnce()).addFlashAttribute(KeyWordsConstants.KEY_MESSAGE, gotMessage);
        verify(redirectAttributes, atLeastOnce())
                .addFlashAttribute(KeyWordsConstants.KEY_INFO_LEVEL, MessagesConstants.INFO_LEVEL_INFO);
    }

    @Test
    @DisplayName("写真削除_異常系_サービスから例外をキャッチ")
    void deletePhotoE1() throws PhotosBusinessException {

        int id = 1;
        String exceptionMessage = "exceptionMessage";
        String infoLevel = "infoLevel";
        Locale locale = Locale.getDefault();
        String gotMessage = "gotMessage";
        doThrow(new PhotosBusinessException(exceptionMessage, infoLevel))
                .when(photosService).deletePhotos(new int[]{id});
        doReturn(locale).when(httpServletRequest).getLocale();
        doReturn(gotMessage).when(messageSource).getMessage(exceptionMessage, null, locale);
        String actual = photosController.deletePhoto(redirectAttributes, id, httpServletRequest);
        assertEquals(UrlsConstants.REDIRECT_GET_PHOTOS, actual);
        verify(redirectAttributes, atLeastOnce()).addFlashAttribute(KeyWordsConstants.KEY_MESSAGE, gotMessage);
        verify(redirectAttributes, atLeastOnce()).addFlashAttribute(KeyWordsConstants.KEY_INFO_LEVEL, infoLevel);
    }
}
